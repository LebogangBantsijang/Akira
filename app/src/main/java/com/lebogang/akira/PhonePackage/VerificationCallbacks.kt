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

package com.lebogang.akira.PhonePackage

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    lateinit var phoneCustomInterface:PhoneCustomInterface
    lateinit var credential: PhoneAuthCredential
    private lateinit var verificationId:String
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken

    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
        super.onCodeSent(p0, p1)
        verificationId = p0
        forceResendingToken = p1
        phoneCustomInterface.onPhoneCallback(Feedback.CODE_SENT)
    }

    override fun onCodeAutoRetrievalTimeOut(p0: String) {
        super.onCodeAutoRetrievalTimeOut(p0)
        phoneCustomInterface.onPhoneCallback(Feedback.TIMEOUT)
    }

    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
        credential = p0
        phoneCustomInterface.onPhoneCallback(Feedback.VERIFICATION_COMPLETED)
    }

    override fun onVerificationFailed(p0: FirebaseException) {
        phoneCustomInterface.onPhoneCallback(Feedback.VERIFICATION_FAILED)
    }

    fun getCredential(code: String):PhoneAuthCredential{
        return PhoneAuthProvider.getCredential(verificationId, code)
    }

    enum class Feedback{
        CODE_SENT, VERIFICATION_COMPLETED, VERIFICATION_FAILED, TIMEOUT
    }
}