package com.janewaitara.medec.di

import com.janewaitara.medec.ui.authentication.login.LoginActivity
import com.janewaitara.medec.ui.authentication.register.RegisterActivity
import com.janewaitara.medec.common.utils.CredentialsValidator
import com.janewaitara.medec.common.utils.Validator
import org.koin.dsl.module

val applicationModule = module(true) {
    scope<RegisterActivity> {
        scoped<Validator> {
                       get()}
    }
    scope<LoginActivity> {
        scoped<Validator> {
                       get()}
    }

    factory { CredentialsValidator() }
}
