package com.ayu.archetecture.todoapp.tasks.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ayu.archetecture.todoapp.tasks.data.entity.Task

/**
 * Author: allenwang
 * Date: 2021/9/14 16:49
 * Description: Tasks表的增删改查
 */
@Dao
interface TaskDao {

    @Query("select * from Tasks")
    fun observeTasks(): LiveData<List<Task>>

    @Query("select * from Tasks where entityId = :taskId")
    fun observeTaskById(taskId: String): LiveData<Task>

    @Query("select * from Tasks")
    suspend fun getTasks(): List<Task>

    @Query("select * from Tasks where entityId = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("update Tasks set completed = :isCompleted where entityId = :taskId")
    suspend fun updateCompleted(taskId: String, isCompleted: Boolean)


    @Query("delete from Tasks where entityId = :taskId")
    suspend fun deleteTaskById(taskId: String): Int

    @Query("delete from Tasks")
    suspend fun deleteTasks()

    @Query("delete from Tasks where completed = 1")
    suspend fun deleteCompletedTasks()

}