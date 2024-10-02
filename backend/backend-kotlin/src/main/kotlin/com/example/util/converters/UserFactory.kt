package com.example.util.converters

import com.example.features.user.business.model.*
import com.example.features.user.data.entity.*
import com.example.features.user.data.model.*
import com.example.features.user.dtos.*
import com.example.util.enums.*
import org.jetbrains.exposed.sql.*

class UserFactory : Converter<UserModel> {

    /**
     * Method that converts the result row into a businessModel
     * @author Reshwan Barhoe
     * @param row ResultRow of the query
     * @return the business user model
     */
    override fun convertToBusinessModel(row: ResultRow): UserModel {
        return UserModel(
            id = row[User.id].value,
            firstName = row[User.firstName],
            prefixes = row[User.prefixes],
            lastName = row[User.lastName],
            role = row[User.role],
            email = row[User.email],
            password = row[User.password]
        )
    }

    /**
     * converts the user dto to User Model
     * @param createUserDTO the User DTO model
     * @author Ömer Aynaci
     * @return the user model instance
     */
    fun convertToUserModel(createUserDTO: CreateUserDTO): UserModel {
        return UserModel(
            firstName = createUserDTO.firstName,
            lastName = createUserDTO.lastName,
            role = Roles.MEMBER,
            prefixes = createUserDTO.prefixes,
            email = createUserDTO.email,
            password = createUserDTO.password
        )
    }

    /**
     * converts the update user dto to update user model
     * @param updateUserDTO the update user DTO model
     * @author Ömer Aynaci
     * @return the update user model
     */
    fun convertToUserModel(updateUserDTO: UpdateUserDTO): UserModel {
        return UserModel(
            firstName = updateUserDTO.firstName,
            lastName = updateUserDTO.lastName,
            prefixes = updateUserDTO.prefixes,
            email = updateUserDTO.email,
            role = updateUserDTO.role
        )
    }

    /**
     * converts the Update password dto to update password user model
     * @author Ömer Aynaci
     * @param updatePasswordDTO the update password dto
     */
    fun convertToUserModel(updatePasswordDTO: UpdatePasswordDTO): UserModel {
        return UserModel(
            password = updatePasswordDTO.newPassword
        )
    }

    /**
     * Method that converts the result row into a user details DTO
     * @author Reshwan Barhoe
     * @param row ResultRow of the query
     * @return the UserDetailsDTO
     */
    fun convertToUserDetails(row: ResultRow): UserDetails {
        return UserDetails(
            firstName = row[User.firstName],
            prefixes = row[User.prefixes],
            lastName = row[User.lastName],
            email = row[User.email],
        )
    }

    /**
     * Method that converts the result row into a UserSummary object
     * @author Reshwan Barhoe
     * @param row result row that's being converted
     * @return a UserSummary
     */
    fun convertToUserSummary(row: ResultRow): UserSummary {
        return UserSummary(
            id = row[User.id].value,
            firstName = row[User.firstName],
            prefixes = row[User.prefixes],
            lastName = row[User.lastName]
        )
    }

    /**
     * Method that converts the user entity into a UserSummary object
     * @author Reshwan Barhoe
     * @param userEntity that's being converted
     * @return a UserSummary
     */
    fun convertToUserSummary(userEntity: UserEntity): UserSummary {
        return UserSummary(
            id = userEntity.id.value,
            firstName = userEntity.firstName,
            prefixes = userEntity.prefixes,
            lastName = userEntity.lastName
        )
    }
}