package com.janewaitara.medec.di

import com.janewaitara.medec.authentication.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module(true) {

  /* fun provideValidator(): CredentialsValidator = CredentialsValidator()

   factory { provideValidator() }*/

  viewModel { RegisterViewModel(get()) }
}