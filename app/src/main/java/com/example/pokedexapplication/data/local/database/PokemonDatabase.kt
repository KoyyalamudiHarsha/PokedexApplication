package com.example.pokedexapplication.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.pokedexapplication.data.local.PokemonEntity
import com.example.pokedexapplication.data.local.RemoteKeys

// --- DAOs ---

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    /**
     * MODIFICATION:
     * This query now filters by name using the provided search query.
     * The 'LIKE' operator with '%' wildcards finds any name containing the query.
     */
    @Query("SELECT * FROM pokemon WHERE name LIKE :query ORDER BY id ASC")
    fun pagingSource(query: String): PagingSource<Int, PokemonEntity>

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()
}

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE pokemonId = :pokemonId")
    suspend fun remoteKeysPokemonId(pokemonId: Int): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}

// --- Database (Unchanged) ---

@Database(
    entities = [PokemonEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
