package com.lebogang.akira.PhonePackage

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    lateinit var phoneCustomInterface:PhoneCustomInterface
    private lateinit var credential: PhoneAuthCredential
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
        if (credential != null)
            return credential
        else
            return PhoneAuthProvider.getCredential(verificationId!!, code)
    }

    enum class Feedback{
        CODE_SENT, VERIFICATION_COMPLETED, VERIFICATION_FAILED, TIMEOUT
    }
}