package com.tofiq.blood.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit>
    suspend fun registerWithEmailPassword(email: String, password: String): Result<Unit>
    fun isLoggedIn(): Boolean
    fun signOut()
}

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit> =
        runCatching {
            firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            Unit
        }

    override suspend fun registerWithEmailPassword(email: String, password: String): Result<Unit> =
        runCatching {
            firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            Unit
        }

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun signOut() {
        firebaseAuth.signOut()
    }
}


