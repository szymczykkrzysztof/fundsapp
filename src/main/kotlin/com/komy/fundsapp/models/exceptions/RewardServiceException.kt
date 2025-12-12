package com.komy.fundsapp.models.exceptions

/*
 * Thrown when an error occurs in the reward service
 */
class RewardServiceException(message: String? = "Reward service is not available") : RuntimeException(message)