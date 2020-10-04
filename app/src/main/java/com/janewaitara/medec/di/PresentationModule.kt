package com.janewaitara.medec.di

import com.janewaitara.medec.ui.authentication.login.LoginViewModel
import com.janewaitara.medec.ui.authentication.register.RegisterViewModel
import com.janewaitara.medec.ui.location.LocationViewModel
import com.janewaitara.medec.ui.patients.account.AccountsViewModel
import com.janewaitara.medec.ui.patients.allDoctors.AllDoctorsViewModel
import com.janewaitara.medec.ui.patients.chatMessaging.ChatMessagingViewModel
import com.janewaitara.medec.ui.patients.chats.ChatsViewModel
import com.janewaitara.medec.ui.patients.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module(true) {

  /* fun provideValidator(): CredentialsValidator = CredentialsValidator()

   factory { provideValidator() }*/

  viewModel { RegisterViewModel(get(),get()) }
  viewModel { LoginViewModel(get(), get()) }
  viewModel { LocationViewModel(get()) }
  viewModel { HomeViewModel(get())}
  viewModel { AllDoctorsViewModel(get()) }
  viewModel { AccountsViewModel(get()) }
  viewModel { ChatMessagingViewModel(get()) }
  viewModel { ChatsViewModel(get()) }
}