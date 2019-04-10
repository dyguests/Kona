package com.fanhl.kona.base.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class DaggerViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value
        ?: throw IllegalArgumentException("Can't find ViewModel to inject ($modelClass) - perhaps it's not added to ViewModelModule?")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: ClassCastException) {
            throw IllegalStateException("Something went terribly wrong while instantiating a ViewModel", e)
        }
    }
}
