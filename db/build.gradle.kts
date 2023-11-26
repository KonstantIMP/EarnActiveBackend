import io.gitlab.arturbosch.detekt.Detekt
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Jdbc
import org.jooq.meta.jaxb.Property
import org.jooq.meta.jaxb.Target
import org.testcontainers.containers.PostgreSQLContainerProvider
import java.sql.DriverManager

plugins {
    kotlin("jvm")
    id("java-library")
    alias(libs.plugins.jooq)
}

dependencies {
    implementation(libs.jooq)
}

buildscript {
    dependencies {
        classpath(libs.h2)
        classpath(libs.psql)
        classpath(libs.psql.tc)
        classpath(libs.jooq.liquibase)
    }
}

tasks.withType<Detekt>().configureEach {
    exclude("**/db/**")
}

fun runJooqGeneration(
    driver: String,
    jdbcUrl: String,
    database: Database,
    dbUser: String = "developer",
    dbPassword: String = "password"
) {
    val configuration = Configuration()
        .withJdbc(
            Jdbc().withDriver(driver)
                .withUrl(jdbcUrl)
                .withUser(dbUser)
                .withPassword(dbPassword)
        )
        .withGenerator(
            Generator().withName("org.jooq.codegen.KotlinGenerator")
                .withDatabase(database)
                .withGenerate(
                    Generate().withPojos(true)
                        .withPojosAsKotlinDataClasses(true)
                        .withDaos(true)
                )
                .withTarget(
                    Target().withPackageName("org.kimp.earnactive.db")
                        .withDirectory("${project.projectDir}/src/main/kotlin")
                )
        )
    GenerationTool.generate(configuration)
}

fun runLiquibase(
    jdbcUrl: String,
    dbUser: String,
    dbPassword: String
) {
    DriverManager.getConnection(jdbcUrl, dbUser, dbPassword).use { c ->
        val database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(JdbcConnection(c))

        val liquibase = Liquibase(
            "changelog.xml",
            DirectoryResourceAccessor(project.projectDir.resolve("src").resolve("main").resolve("resources")),
            database
        )
        liquibase.update()
    }
}

val jooqByH2 = tasks.create("jooqByH2") {
    doLast {
        runJooqGeneration(
            "org.h2.Driver",
            "jdbc:h2:mem:jooqdb;MODE=PostgreSQL;",
            Database().withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
                .withProperties(
                    Property()
                        .withKey("rootPath")
                        .withValue("${project.projectDir}/src/main/resources"),
                    Property()
                        .withKey("scripts")
                        .withValue("h2_changelog.xml"),
                    Property()
                        .withKey("includeLiquibaseTables")
                        .withValue("false"),
                )
        )
    }
}

val jooqByPostgres = tasks.create("jooqByPostgres") {
    doLast {
        val psql = PostgreSQLContainerProvider().newInstance()
        psql.start()

        runLiquibase(
            psql.jdbcUrl,
            psql.username,
            psql.password
        )

        runJooqGeneration(
           "org.postgresql.Driver",
            psql.jdbcUrl,
            Database().withName("org.jooq.meta.postgres.PostgresDatabase")
                .withInputSchema("public")
                .withExcludes("databasechangelog|databasechangeloglock"),
            psql.username,
            psql.password
        )

        psql.stop()
    }
}

tasks.create("runLiquibase") {
    doLast {
        val dbAddr = System.getenv("EA_DATABASE_ADDR") ?: ""
        val dbName = System.getenv("EA_DATABASE_NAME") ?: ""
        val dbUser = System.getenv("EA_DATABASE_USER") ?: ""
        val dbPass = System.getenv("EA_DATABASE_PWD") ?: ""

        if (listOf(dbAddr, dbName, dbUser, dbPass).any { it.isEmpty() }) {
            println("To run migration specify environments")
            return@doLast
        }

        val jdbcUrl = "jdbc:postgresql://$dbAddr/$dbName"
        println("Going to run migration for $jdbcUrl")

        runLiquibase(jdbcUrl, dbUser, dbPass)
    }
}
