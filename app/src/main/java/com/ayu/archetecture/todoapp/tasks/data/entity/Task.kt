package com.ayu.archetecture.todoapp.tasks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Author: allenwang
 * Date: 2021/9/13 14:37
 * Description: 任务表实体
 */
@Entity(tableName = "tasks")
class Task @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "entityId")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "completed")
    val isCompleted: Boolean = false
    ) {

    val isActive: Boolean
        get() = !isCompleted

    val isEmpty: Boolean
        get() = title.isEmpty() || description.isEmpty()

    val titleForList: String
        get() = if (title.isEmpty()) description else title

    override fun equals(other: Any?): Boolean {
        (other as? Task)?.let {
            return id == it?.id &&
                    isCompleted == it.isCompleted &&
                    title == it.titleForList &&
                    description == it.description
        }
        return false
    }
}