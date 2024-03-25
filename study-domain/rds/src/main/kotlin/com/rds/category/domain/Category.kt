package com.rds.category.domain

import com.rds.BaseTimeEntity
import jakarta.persistence.*


@Entity
class Category (
    val name: String,
    val depth: Int
) : BaseTimeEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private val parent: Category? = null

    @OneToMany(mappedBy = "parent")
    private val child: List<Category> = ArrayList()
}