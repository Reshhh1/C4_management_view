package com.example.frontend_android.users.domain.use_case

import com.example.frontend_android.users.data.remote.dtos.request.RegistrationRequest
import com.example.frontend_android.users.data.repository.UserRepository
import com.example.frontend_android.users.domain.model.RegistrationResult
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    /**
     * Use case for creating a new user
     * @author Ömer Aynaci
     * @param user the registration request body
     */
    suspend fun createUser(user: RegistrationRequest): RegistrationResult<String> {
        return userRepository.createUser(user)
    }
}