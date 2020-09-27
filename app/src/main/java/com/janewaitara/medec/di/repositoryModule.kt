package com.janewaitara.medec.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.janewaitara.medec.repository.FirebaseRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseRepository(get(), get()) }
}