/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.backend.config.database

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class ReadOnlyRoutingDataSource : AbstractRoutingDataSource() {

    override fun determineCurrentLookupKey(): Any? {
        val readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()

        return if (readOnly) LookUpKey.READER else LookUpKey.WRITER
    }
}
