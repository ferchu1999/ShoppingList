package com.example.supermarketapp.activities.holders

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.supermarketapp.R
import com.example.supermarketapp.activities.dataclass.Product
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ListProductHolder(view: View): RecyclerView.ViewHolder(view) {

    private val checkBoxProducts: CheckBox = view.findViewById(R.id.mcb_check)
    private val productName: MaterialTextView = view.findViewById(R.id.mtv_text_product)
    private val deleteProduct: ImageButton = view.findViewById(R.id.ib_delete)
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    fun render(product: Product, onClickDelete: (Int, String) -> Unit){
        productName.text = product.name

       checkBoxProducts.setOnClickListener {
           checkProduct(product.idProduct)
       }

        updateChecked(product.idProduct)
        deleteProduct.setOnClickListener { onClickDelete(adapterPosition,product.idProduct) }
    }
    private fun checkProduct(doc: String) {

        if (checkBoxProducts.isChecked){

            productName.paintFlags = productName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            db.collection("Users").document(auth.currentUser!!.uid)
                .collection("Products").document(doc)
                .update("isSelected", true)
        }else{

            productName.paintFlags = productName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            db.collection("Users").document(auth.currentUser!!.uid)
                .collection("Products").document(doc)
                .update("isSelected", false)
        }
    }

    private fun updateChecked(doc: String) {

        db.collection("Users")
            .document(auth.currentUser!!.uid)
            .collection("Products").document(doc).get().addOnSuccessListener { value ->
                checkBoxProducts.isChecked = value.get("isSelected").toString().toBoolean()

                if (checkBoxProducts.isChecked)
                    productName.paintFlags = productName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else
                    productName.paintFlags = productName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
    }

}