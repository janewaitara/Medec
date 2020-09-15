package com.janewaitara.medec.di

import com.google.firebase.firestore.FirebaseFirestore
import com.janewaitara.medec.repository.FirebaseRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { FirebaseFirestore.getInstance() }

    single { FirebaseRepository(get()) }
}