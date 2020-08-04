package com.ekochkov.intervallearning.room

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getAll(): List<Word>
	
	@Query("SELECT * FROM word ORDER BY id ASC")
    fun getAllGroupById(): List<Word>
	
	@Query("SELECT * FROM word GROUP BY category")
    fun getAllCategories(): List<Word>
	
	@Query("SELECT * FROM word WHERE category = (:category)")
    fun getByCategory(category: String?): List<Word>
	
	@Query("SELECT * FROM word WHERE id = (:id)")
    fun getById(id: Integer?): Maybe<Word>

    @Query("SELECT * FROM word WHERE original = (:originalWord)")
    fun getByOriginal(originalWord: String?): Maybe<Word>
	
	@Query("SELECT * FROM word WHERE category = (:category) AND original LIKE (:search)")
	fun searchByOriginal(category: String, search: String): List<Word>

    @Query("SELECT * FROM word WHERE category = (:category) AND original LIKE (:search) OR translate LIKE (:search)")
    fun searchByOriginalOrTranslate(category: String, search: String): List<Word>
	
	@Update
	fun update(word: Word?): Int

    @Update
    fun updateSync(words: List<Word>): Int
	
	@Delete
    fun delete(word: Word?): Maybe<Int>

    @Query("DELETE FROM word WHERE id = :id")
    fun deleteById(id: Int?): Maybe<Int>

    @Insert
    fun insert(word: Word?): Long
}