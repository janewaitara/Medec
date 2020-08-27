package com.janewaitara.medec.di

import com.janewaitara.medec.ui.authentication.login.LoginFragment
import com.janewaitara.medec.ui.authentication.register.RegisterFragment
import com.janewaitara.medec.common.utils.CredentialsValidator
import com.janewaitara.medec.common.utils.Validator
import org.koin.dsl.module

val applicationModule = module(true) {
    scope<RegisterFragment> {
        scoped<Validator> {
                       get()}
    }
    scope<LoginFragment> {
        scoped<Validator> {
                       get()}
    }

    factory { CredentialsValidator() }
}
