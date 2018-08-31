package habitbuilder.f.tom.makes.com.habitbuilder

import android.app.Application
import android.util.Log
import com.couchbase.lite.*
import com.couchbase.lite.Array


val METADATA_DOC_ID = "MetaData"
val DATABASE_NAME = "habitBuilderDB"
val HABITS_INDEX_DOC_ID = "habitIndexDoc"
val HABITS_INDEX_ARRAY_ID =  "habitIndex"

class HabitBuilderApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        initCouchBaseIfNeeded()
    }

    private fun initCouchBaseIfNeeded(){
        val config = DatabaseConfiguration(applicationContext)
        val database = Database(DATABASE_NAME, config)
        if (database.getDocument(METADATA_DOC_ID) == null) {
            // Create a new document if it doesnt exist yet.
            val mutableDoc = MutableDocument(METADATA_DOC_ID)
                    .setInt("version", 1)
            database.save(mutableDoc)
            Log.i(this.toString(), "Writing initial database metadata")
        }

        if (database.getDocument(HABITS_INDEX_DOC_ID) == null){
            val doc= MutableDocument(HABITS_INDEX_DOC_ID)
            //todo create empty index

        }

    }

}