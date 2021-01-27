package com.udacity.locationreminder.database.entity

import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

@Entity(tableName = "reminders")
@Parcelize
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "request_id") var requestId: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "location_name") var locationName: String,
    @ColumnInfo(name = "coordinate") var coordinate: Coordinate,
    @ColumnInfo(name = "radius") var radius: Int = 0,
    @ColumnInfo(name = "status") var status: ReminderStatus = ReminderStatus.ACTIVE,
    @ColumnInfo(name = "last_modified") var modificationDate: LocalDateTime = LocalDateTime.now()
) : Parcelable {

    companion object {
        fun initial() =
            ReminderEntity(
                0, "", "", "", "", Coordinate(0.0, 0.0)
            )

        @VisibleForTesting
        fun forTesting() = ReminderEntity(
            1, "test", "title", "description", "location",
            Coordinate(25.0, 25.0), 15
        )
    }

    fun hasEmptyFields(): Boolean {
        return title.isEmpty() || description.isEmpty() || coordinate.isIncomplete()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReminderEntity

        if (id != other.id) return false
        if (requestId != other.requestId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (locationName != other.locationName) return false
        if (coordinate != other.coordinate) return false
        if (radius != other.radius) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + requestId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + locationName.hashCode()
        result = 31 * result + coordinate.hashCode()
        result = 31 * result + radius
        return result
    }
}