package com.example.supermarketapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.supermarketapp.R
import com.example.supermarketapp.databinding.ActivityAddProductsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*

class AddProductActivity : AppCompatActivity() {

    private lateinit var bindingAddProduct: ActivityAddProductsBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAddProduct = ActivityAddProductsBinding.inflate(layoutInflater)
        setContentView(bindingAddProduct.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        autoCompleteTextProducts()
        bottomMenuButtons()
        addProductButton()
        loadImage()
    }

    private fun loadImage() {
        val url = "https://i.pinimg.com/564x/53/28/a0/5328a07c31b0f11b73c1e18b7853f427.jpg"

        Picasso.get().load(url)
            .resize(150,150)
            .centerCrop()
            .into(bindingAddProduct.ivLogoAddProduct)
    }

    private fun addProductButton() {
        bindingAddProduct.mbButtonProducts.setOnClickListener {

            val randomID: String = UUID.randomUUID().toString()

            if (bindingAddProduct.tieNameProduct.text!!.isNotEmpty()
                && bindingAddProduct.matCategoryProd.text!!.isNotEmpty()
                && bindingAddProduct.tieQuantityProduct.text!!.isNotEmpty()
                && bindingAddProduct.tieKgUnits.text!!.isNotEmpty()
                && bindingAddProduct.tiePriceProduct.text!!.isNotEmpty()
            ) {


                db.collection("Users").document(auth.currentUser!!.uid)
                    .collection("Products").document(randomID)
                    .set(
                    hashMapOf(
                        "name" to bindingAddProduct.tieNameProduct.text.toString(),
                        "category" to bindingAddProduct.matCategoryProd.text.toString(),
                        "quantity" to bindingAddProduct.tieQuantityProduct.text.toString().toFloat(),
                        "units" to bindingAddProduct.tieKgUnits.text.toString().toInt(),
                        "price" to bindingAddProduct.tiePriceProduct.text.toString().toFloat(),
                        "isSelected" to false
                    )
                )

               Snackbar.make(it,"Producto añadido",Snackbar.LENGTH_SHORT).show()


            }else{
                Snackbar.make(it,"Error al añadir el producto",Snackbar.LENGTH_LONG).show()
            }

            clearFields()
        }
    }

    private fun clearFields() {
        bindingAddProduct.tieNameProduct.text?.clear()
        bindingAddProduct.matCategoryProd.text?.clear()
        bindingAddProduct.tieQuantityProduct.text?.clear()
        bindingAddProduct.tieKgUnits.text?.clear()
        bindingAddProduct.tiePriceProduct.text?.clear()
    }


    private fun autoCompleteTextProducts() {

        val categories: Array<String> = arrayOf(
            "Carnes", "Pescado y mariscos",
            "Fruta y verdura", "Lacteos"
        )

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, categories
        )

        bindingAddProduct.matCategoryProd.setAdapter(arrayAdapter)

    }

    private fun bottomMenuButtons() {

        bindingAddProduct.bnwBottom.selectedItemId = R.id.grocery_prods

        bindingAddProduct.bnwBottom.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.grocery_prods -> return@setOnItemSelectedListener true

                R.id.list_products -> {

                    val intentPurchase = Intent(this, ProductList::class.java)
                    startActivity(intentPurchase)
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }

            false
        }
    }
}