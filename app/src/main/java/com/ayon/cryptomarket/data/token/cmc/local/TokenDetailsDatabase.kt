package com.ayon.cryptomarket.data.token.cmc.local

import android.content.Context
import androidx.room.*
import com.ayon.cryptomarket.domain.TokenDetails

private const val TOKEN_DETAILS_TABLE = "token_details_table"

@Entity(tableName = TOKEN_DETAILS_TABLE)
data class TokenDetailsEntity(
    @PrimaryKey @ColumnInfo(name = "symbol")val symbol: String,
    val name: String,
    val logo: String)


@Dao
interface TokenDetailsDao {

    @Query("SELECT * FROM token_details_table ORDER BY name ASC")
    suspend fun getTokenDetails(): List<TokenDetailsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tokenDetails: TokenDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tokenDetails: List<TokenDetailsEntity>)

    @Query("DELETE FROM TOKEN_DETAILS_TABLE")
    suspend fun deleteAll()
}

@Database(entities = [TokenDetailsEntity::class], version = 1, exportSchema = false)
abstract class TokenDetailsDatabase : RoomDatabase() {

    abstract fun tokenDetailsDao(): TokenDetailsDao

    companion object {

        @Volatile
        private var INSTANCE: TokenDetailsDatabase? = null

        fun getDatabase(context: Context): TokenDetailsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TokenDetailsDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}


fun TokenDetailsEntity.toTokenDetails() = TokenDetails(symbol = symbol, name = name, logo = logo)

fun TokenDetails.toDatabaseEntity() = TokenDetailsEntity(symbol = symbol, name = name, logo = logo)