package com.janewaitara.medec.model

/**hold user Ids and collections of all msgs in firestore
 * but collections in documents are in firestore only */

data class ChatChannel(val userIds: MutableList<String> = mutableListOf()) {
    /*constructor(): this(mutableListOf())*/
}