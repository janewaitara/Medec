package com.janewaitara.medec.di

import com.janewaitara.medec.authentication.RegisterActivity
import com.janewaitara.medec.common.utils.CredentialsValidator
import com.janewaitara.medec.common.utils.Validator
import org.koin.dsl.module

val applicationModule = module {

        scope<Validator> { CredentialsValidator()}
}