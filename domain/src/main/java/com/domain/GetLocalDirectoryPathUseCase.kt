package com.domain

import javax.inject.Inject

class GetLocalDirectoryPathUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    fun execute(): String? {
        return repository.getLocalDirectoryPath()
    }
}
