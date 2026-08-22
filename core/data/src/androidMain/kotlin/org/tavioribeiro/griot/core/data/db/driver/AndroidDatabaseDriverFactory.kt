package org.tavioribeiro.griot.core.data.db.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.tavioribeiro.griot.core.data.db.GriotDatabase

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {

    override fun createDriver(): SqlDriver =
        AndroidSqliteDriver(GriotDatabase.Schema, context, "griot.db")
}
