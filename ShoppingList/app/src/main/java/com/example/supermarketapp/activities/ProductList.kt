package com.example.supermarketapp.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.supermarketapp.R
import com.example.supermarketapp.activities.adapters.ListProductAdapter
import com.example.supermarketapp.activities.dataclass.Product
import com.example.supermarketapp.databinding.ActivityProductsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductList : AppCompatActivity(){

    private lateinit var bindingProductList: ActivityProductsBinding
    private val db = FirebaseFirestore.getInstance()
    private val listProductsName: MutableList<Product> = mutableListOf()
    private lateinit var adapter: ListProductAdapter
    private lateinit var auth: FirebaseAuth

    //Lazy Load
    //During the first invocation executes the lambda passed to it and in subsequent
    // invocations it will return the value initially computed.
    private val onBackInvokedCallback by lazy {
       exitApp()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProductList = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(bindingProductList.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        bottomMenuButtons()
        initRecyclerView()

        onBackPressedDispatcher.addCallback(this, onBackInvokedCallback)

        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                saveDataPreferences(auth.currentUser!!.email.toString())
            }
        }

    }

    private fun initRecyclerView() {
            adapter = ListProductAdapter(
            listP = listProductsName,
            onClickDelete = { position, idDocument -> onDeletedItemProduct(position, idDocument) })

            bindingProductList.rvProducts.layoutManager = LinearLayoutManager(this)
            bindingProductList.rvProducts.adapter = adapter


        db.collection("Users").document(auth.currentUser!!.uid)
            .collection("Products")
            .get().addOnSuccessListener { documents ->

            for(document in documents ){
               val product = Product(
                   idProduct = document.id,
                   name = document.get("name").toString(),
                   category = document.get("category").toString(),
                   quantity = document.get("quantity").toString().toFloat(),
                   units = document.get("units").toString().toInt(),
                   price = document.get("price").toString().toFloat()
               )
                listProductsName.add(product)
            }
            adapter = ListProductAdapter(
                listP =  listProductsName,
                onClickDelete = { position,idDocument -> onDeletedItemProduct(position, idDocument) }
            )
            bindingProductList.rvProducts.adapter = adapter
        }

    }

    private fun saveDataPreferences(email: String) {
        //Save data
        val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()

        prefs.putString("email", email)
        prefs.apply()
    }

    private fun onDeletedItemProduct(position: Int, doc: String) {
        db.collection("Users").document(auth.currentUser!!.uid)
            .collection("Products").document(doc).delete()
        listProductsName.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun bottomMenuButtons() {

        bindingProductList.bnvBottomProducts.selectedItemId = R.id.list_products

        bindingProductList.bnvBottomProducts.setOnItemSelectedListener { item ->

            when(item.itemId){

                R.id.list_products -> return@setOnItemSelectedListener true

                R.id.grocery_prods -> {

                    val intentPurchase = Intent(this, AddProductActivity::class.java)
                    startActivity(intentPurchase)
                    overridePendingTransition(0,0)
                    return@setOnItemSelectedListener true
                }
            }

            false
        }
    }


    private fun exitApp(): OnBackPressedCallback {

        return object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val alertDialogBuilder = AlertDialog.Builder(this@ProductList)
                alertDialogBuilder.setMessage("¿Quieres salir de la aplicación?")
                alertDialogBuilder.setPositiveButton("Si"){ _ , _ ->
                    finishAffinity()
                }

                alertDialogBuilder.setNegativeButton("No"){ dialog, _ ->
                    dialog.cancel()
                }
                alertDialogBuilder.show()
            }

        }
    }

}