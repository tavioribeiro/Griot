package org.tavioribeiro.griot.core.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties

class JvmDatabaseDriverFactory(
    private val databasePath: String = ":memory:"
) : DatabaseDriverFactory {

    override fun createDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:$databasePath", Properties(), GriotDatabase.Schema)
}