package com.example.dogcare

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import PetsAdapter

object FirebaseUtils {

    fun fetchPets(db: FirebaseFirestore, petsAdapter: PetsAdapter) {
        db.collection("pets")
            .get()
            .addOnSuccessListener { documents ->
                val petsList = mutableListOf<String>()
                for (document in documents) {
                    val petName = document.getString("petName")
                    petName?.let {
                        petsList.add(it)
                    }
                }
                petsAdapter.submitList(petsList)
            }
            .addOnFailureListener { exception ->
            }
    }

    fun deletePet(db: FirebaseFirestore, petName: String): Task<Void> {
        val query = db.collection("pets").whereEqualTo("petName", petName)
        return query.get().continueWithTask { task ->
            if (task.isSuccessful) {
                val batch = db.batch()
                for (document in task.result!!) {
                    batch.delete(document.reference)
                }
                return@continueWithTask batch.commit()
            } else {
                throw task.exception!!
            }
        }
    }


    fun fetchSinglePet(db: FirebaseFirestore, petName: String, onPetFetched: (Map<String, Any>?) -> Unit) {
        db.collection("pets")
            .whereEqualTo("petName", petName)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val petData = document.data
                    onPetFetched(petData)
                } else {
                    onPetFetched(null)
                }
            }
            .addOnFailureListener { exception ->
                onPetFetched(null)
            }
    }
}
