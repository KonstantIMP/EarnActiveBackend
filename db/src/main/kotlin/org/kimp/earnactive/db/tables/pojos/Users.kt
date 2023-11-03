/*
 * This file is generated by jOOQ.
 */
package org.kimp.earnactive.db.tables.pojos


import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class Users(
    var uuid: UUID? = null,
    var phone: String? = null,
    var name: String? = null,
    var creationTimestamp: LocalDateTime? = null
): Serializable {


    public override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (this::class != other::class)
            return false
        val o: Users = other as Users
        if (this.uuid == null) {
            if (o.uuid != null)
                return false
        }
        else if (this.uuid != o.uuid)
            return false
        if (this.phone == null) {
            if (o.phone != null)
                return false
        }
        else if (this.phone != o.phone)
            return false
        if (this.name == null) {
            if (o.name != null)
                return false
        }
        else if (this.name != o.name)
            return false
        if (this.creationTimestamp == null) {
            if (o.creationTimestamp != null)
                return false
        }
        else if (this.creationTimestamp != o.creationTimestamp)
            return false
        return true
    }

    public override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (if (this.uuid == null) 0 else this.uuid.hashCode())
        result = prime * result + (if (this.phone == null) 0 else this.phone.hashCode())
        result = prime * result + (if (this.name == null) 0 else this.name.hashCode())
        result = prime * result + (if (this.creationTimestamp == null) 0 else this.creationTimestamp.hashCode())
        return result
    }

    public override fun toString(): String {
        val sb = StringBuilder("Users (")

        sb.append(uuid)
        sb.append(", ").append(phone)
        sb.append(", ").append(name)
        sb.append(", ").append(creationTimestamp)

        sb.append(")")
        return sb.toString()
    }
}