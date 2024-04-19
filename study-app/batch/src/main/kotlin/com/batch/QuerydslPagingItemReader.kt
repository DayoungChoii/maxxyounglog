package com.batch

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.util.ClassUtils
import org.springframework.util.CollectionUtils
import java.util.concurrent.CopyOnWriteArrayList

open class QuerydslPagingItemReader<T>(
    private val entityManagerFactory: EntityManagerFactory,
    private val pageSize: Int,
    private val queryFunction: (JPAQueryFactory) -> JPAQuery<T>
) : AbstractPagingItemReader<T>() {
    private val jpaPropertyMap: Map<String?, Any?> = HashMap()
    private var transacted = true //default value
    private lateinit var entityManager: EntityManager

    init {
        super.setPageSize(pageSize)
        super.setName(ClassUtils.getShortName(QuerydslPagingItemReader::class.java))
    }

    @Throws(Exception::class)
    override fun doOpen() {
        super.doOpen()
        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap)
    }

    @Throws(Exception::class)
    override fun doClose() {
        entityManager.close()
        super.doClose()
    }


    override fun doReadPage() {
        clearIfTransacted()
        val query: JPAQuery<T> = createQuery()
            .offset((page * pageSize).toLong())
            .limit(pageSize.toLong())
        initResults()
        fetchQuery(query)
    }

    private fun clearIfTransacted() {
        if (transacted) {
            entityManager.clear()
        }
    }

    private fun createQuery(): JPAQuery<T> {
        val queryFactory = JPAQueryFactory(entityManager)
        return queryFunction.invoke(queryFactory)
    }

    private fun initResults() {
        if (CollectionUtils.isEmpty(results)) {
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }
    }

    private fun fetchQuery(query: JPAQuery<T>) {
        if (!transacted) {
            val queryResult: List<T> = query.fetch()
            for (entity in queryResult) {
                entityManager.detach(entity)
                results.add(entity)
            }
        } else {
            results.addAll(query.fetch())
        }
    }
}