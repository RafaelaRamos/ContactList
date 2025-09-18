package br.edu.scl.ifsp.sdm.contactlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
// Data source
    private val concatList:MutableList<Contact> = mutableListOf()
    //adapter
    private val contactAdapter:ArrayAdapter<String> by lazy {
        ArrayAdapter(this,android.R.layout.simple_list_item_1,concatList.map { it.toString() })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        fillContacts()
        amb.contactLv.adapter =contactAdapter

    }
    private fun fillContacts(){
        for (i in 1 .. 50){
        concatList.add(Contact(i, "Nome$i","address$i","telefone$i","email$i"))
        }
    }
}