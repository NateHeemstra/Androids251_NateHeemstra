package com.example.assignment82

class ContactRepository(private val dao: ContactDao) {
    fun getContactsSortedByNameAsc() = dao.getContactsSortedByNameAsc()
    fun getContactsSortedByNameDesc() = dao.getContactsSortedByNameDesc()
    fun findContacts(query: String) = dao.findContacts(query)

    suspend fun insert(contact: Contact){
        dao.insert(contact)
    }
    suspend fun delete(contact: Contact){
        dao.delete(contact)
    }
}