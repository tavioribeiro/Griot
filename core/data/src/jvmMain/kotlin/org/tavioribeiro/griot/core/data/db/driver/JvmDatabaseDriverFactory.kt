package org.tavioribeiro.griot.core.data.db.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties
import org.tavioribeiro.griot.core.data.db.GriotDatabase

class JvmDatabaseDriverFactory(
    private val databasePath: String = ":memory:"
) : DatabaseDriverFactory {

    override fun createDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:$databasePath", Properties(), GriotDatabase.Schema)
}
