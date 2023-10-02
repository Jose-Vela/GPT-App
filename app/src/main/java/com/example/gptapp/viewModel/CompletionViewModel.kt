package com.example.gptapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gptapp.CompletionResponse
import com.example.gptapp.model.CompletionInterceptor

class CompletionViewModel : ViewModel() {
    private  var interceptor: CompletionInterceptor = CompletionInterceptor()
    private  var completionLiveData: MutableLiveData<CompletionResponse> = MutableLiveData()

    // observeCompletionLiveData() nos servirá para poder ver si la API ya ha tenido alguna respuesta o error
    fun observeCompletionLiveData(): MutableLiveData<CompletionResponse> {
        return completionLiveData
    }

    // postCompletionLiveData es la que nos permitirá comunicarnos directamente con la API
    fun postCompletionLiveData(promt:String) {
        interceptor.postCompletion(promt){// El promt lo pasamos directamente al interceptor
            // Pasamos mediante it, el completionResponse para retornale al LiveData la respuesta a traves de la función postValue(it)
            completionLiveData.postValue(it)
        }
    }

}