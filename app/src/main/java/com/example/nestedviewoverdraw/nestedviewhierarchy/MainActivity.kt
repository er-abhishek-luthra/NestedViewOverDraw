package com.example.nestedviewoverdraw.nestedviewhierarchy

import android.os.Bundle
import android.os.Trace
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nestedviewoverdraw.R
import com.example.nestedviewoverdraw.nestedviewhierarchy.data.LocalDataSource


class MainActivity : AppCompatActivity() {
    lateinit var customAdapter: CustomAdapter
    lateinit var localDataSource: LocalDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        localDataSource = LocalDataSource()

        // Initialize data.
        val myDataset = localDataSource.getMessages()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        customAdapter = CustomAdapter(this, myDataset)
        recyclerView.adapter = customAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.notify_data_set_changes -> {
                    Trace.beginSection("notifydatasetchanged")
                    val newDataSet =
                        localDataSource.getItemChangedDataSet(" Notify Data Set Changed",1)
                    customAdapter.updateDataSet(newDataSet);
                    customAdapter.notifyDataSetChanged()
                    Trace.endSection()

                }

                R.id.notify_item_changed -> {
                    Trace.beginSection("Notify Item Changed")
                    val newDataSet =
                        localDataSource.getItemChangedDataSet("Notify Item Changed",1)
                    customAdapter.updateDataSet(newDataSet);
                    customAdapter.notifyItemChanged(1)
                    Trace.endSection()

                }
                else -> {

                }
            }
        return true
    }
}