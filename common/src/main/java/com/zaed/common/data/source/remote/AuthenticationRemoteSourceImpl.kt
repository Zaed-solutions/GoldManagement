package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovementStatusType
import com.zaed.common.data.model.request.LoginUserRequest
import com.zaed.common.data.model.request.SignUpUserRequest
import com.zaed.common.ui.component.auth.LoginError
import com.zaed.common.ui.component.auth.signup.SignUpError
import com.zaed.common.ui.component.auth.signup.log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthenticationRemoteSourceImpl(
    firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : AuthenticationRemoteSource {
    private val USERS_COLLECTION = "users"
    private val usersCollection = firestore.collection(USERS_COLLECTION)


    override suspend fun loginUser(request: LoginUserRequest): Flow<Result<User>> = callbackFlow {
        try {
            val filter = Filter.and(
                Filter.equalTo("userName", request.userName),
                Filter.equalTo("password", request.password),
            )

            usersCollection.where(filter).get().addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val user = querySnapshot.documents[0].toObject(User::class.java)
                    trySend(Result.success(user!!))
                } else {
                    trySend(Result.failure(
                        LoginError.UserNotFound()
                    ))
                }
            }.addOnFailureListener { e ->
                val error = LoginError.LoginFailed(
                    reason = e.message.toString(),
                    location = AuthenticationRemoteSourceImpl::class.simpleName + ".loginUser.While checking userName & password"
                )
                crashlytics.recordException(error)
                error.log()
                crashlytics.recordException(error)
                trySend(Result.failure(error))
            }
        } catch (e: Exception) {
            val error = LoginError.LoginFailed(
                reason = e.message.toString(),
                location = AuthenticationRemoteSourceImpl::class.simpleName + ".loginUser.catch"
            )
            crashlytics.recordException(error)
            error.log()
            trySend(Result.failure(e))
        }
        awaitClose { }
    }

    override fun fetchCurrentUser(userId: String): Flow<Result<User>> = callbackFlow {
        try {
            usersCollection.document(userId).addSnapshotListener { value, e ->
                if (e != null) {
                    val error = LoginError.LoginFailed(
                        reason = e.message.toString(),
                        location = AuthenticationRemoteSourceImpl::class.simpleName + ".fetchCurrentUser.While fetching user"
                    )
                    crashlytics.recordException(error)
                    error.log()
                    trySend(Result.failure(error))
                } else {
                    if (value != null && value.exists()) {
                        val user = value.toObject(User::class.java)
                        trySend(Result.success(user!!))
                    } else {
                        val error = LoginError.UserNotFound()
                        crashlytics.recordException(error)
                        trySend(Result.failure(error))
                    }
                }
            }

        }catch (e:Exception){
            val error = LoginError.LoginFailed(
                reason = e.message.toString(),
                location = AuthenticationRemoteSourceImpl::class.simpleName + ".fetchCurrentUser.catch"
            )
            crashlytics.recordException(error)
        }
        awaitClose { }

    }

    override suspend fun signUpUser(request: SignUpUserRequest): Flow<Result<User>> = callbackFlow {
        try {
            usersCollection.whereEqualTo("userName", request.userName).get(Source.SERVER)
                .addOnSuccessListener { querySnapshot ->

                    if (!querySnapshot.isEmpty) {
                        trySend(Result.failure(SignUpError.UserAlreadyExists()))
                    } else {
                        val document = usersCollection.document()
                        val user = User(
                            id = document.id,
                            fullName = request.fullName,
                            userName = request.userName,
                            password = request.password,
                            approvementStatusType = UserApprovementStatusType.PENDING,
                            role = request.role
                        )
                        document.set(user).addOnSuccessListener {
                            trySend(Result.success(user))
                        }.addOnFailureListener { e ->
                            val error = SignUpError.SignUpFailed(
                                reason = e.message.toString(),
                                location = AuthenticationRemoteSourceImpl::class.simpleName + ".signUpUser.While creating user"
                            )
                            crashlytics.recordException(error)
                            error.log()
                            trySend(Result.failure(error))
                        }
                    }
                }.addOnFailureListener { e ->
                    val error = SignUpError.SignUpFailed(
                        reason =e.message.toString(),
                        location = AuthenticationRemoteSourceImpl::class.simpleName + ".signUpUser.While checking user"
                    )
                    crashlytics.recordException(error)
                    error.log()
                    trySend(Result.failure(error))

                }
        } catch (e: Exception) {
            val error = SignUpError.SignUpFailed(
                reason = e.message.toString(),
                location = AuthenticationRemoteSourceImpl::class.simpleName + ".signUpUser.catch"
            )
            crashlytics.recordException(e)
            error.log()
            trySend(Result.failure(error))
        }
        awaitClose { }
    }


}