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

package com.lebogang.akira

import android.text.Editable

class TextUtil {
    companion object{
        fun getMessage(type:ErrorTypes):String{
            return when(type){
                ErrorTypes.NULL_VALUES -> "No blank values"
                ErrorTypes.PASSWORDS_NO_MATCH -> "Passwords do not match"
            }
        }

        fun isValuesNull(vararg editable:Editable?):Boolean{
            editable.forEach {
                if (it.isNullOrEmpty())
                    return true
            }
            return false
        }
    }
    enum class ErrorTypes{
        NULL_VALUES, PASSWORDS_NO_MATCH
    }
}