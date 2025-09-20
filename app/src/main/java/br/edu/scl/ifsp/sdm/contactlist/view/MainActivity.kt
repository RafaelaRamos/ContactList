package br.edu.scl.ifsp.sdm.contactlist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.sdm.contactlist.R
import br.edu.scl.ifsp.sdm.contactlist.adapter.ContactAdapter
import br.edu.scl.ifsp.sdm.contactlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import br.edu.scl.ifsp.sdm.contactlist.model.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val concatList: MutableList<Contact> = mutableListOf()

    //adapter
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(this, concatList)
    }
    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)
        carl =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                    contact?.also { newOreditedContact ->
                        if (concatList.any { it.id == contact.id }) {
                            val position =
                                concatList.indexOfFirst { it.id == newOreditedContact.id }
                            concatList[position] = newOreditedContact

                        } else {
                            concatList.add(newOreditedContact)
                        }
                        contactAdapter.notifyDataSetChanged()
                    }
                }
            }
        fillContacts()
        amb.contactLv.adapter = contactAdapter
        registerForContextMenu(amb.contactLv)

        amb.contactLv.setOnItemClickListener() { parent, view, position, id ->
            startActivity(Intent(this, ContactActivity::class.java).apply {
                putExtra(EXTRA_CONTACT, concatList[position])
                putExtra(EXTRA_VIEW_CONTACT,true)
            })

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.removeContactMi -> {
                concatList.removeAt(position)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(this, getString(R.string.remove_contact), Toast.LENGTH_SHORT).show()
                true
            }

            R.id.editContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java).apply {
                    putExtra(EXTRA_CONTACT, concatList[position])
                })
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.contactLv)
    }

    private fun fillContacts() {
        for (i in 1..10) {
            concatList.add(Contact(i, "Nome$i", "address$i", "telefone$i", "email$i"))
        }
    }
}