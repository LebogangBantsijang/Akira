/*
 * Copyright (c) 2020. - Lebogang Bantsijang
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * compliance License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License on an "IS BASIS", WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the for specific language governing permissions and limitations
 * under the License.
 */

package com.lebogang.akira.FacebookPackage

import android.content.Intent
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.MainActivity
import com.lebogang.akira.UserActivity


class FacebookSignInObject(val activity: MainActivity) {
    val callbackManager = CallbackManager.Factory.create()
    private val auth = Firebase.auth

    fun getFacebookCallback():FacebookCallback<LoginResult>{
        return object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                signIn(result!!.accessToken)
            }
            override fun onCancel() {
            }
            override fun onError(error: FacebookException?) {
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        callbackManager.onActivityResult(requestCode,resultCode,data)
    }

    private fun signIn(token:AccessToken){
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(activity){
            if (it.isSuccessful){
                val user = auth.currentUser
                activity.startActivity(Intent(activity, UserActivity :: class.java).apply {
                    putExtra("User", user)
                })
            }else{
                Snackbar.make(activity.window.peekDecorView()
                        ,"Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}