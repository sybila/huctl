package com.github.sybila.huctl

/**
 * This file contains interfaces that together define a tree structure with unary, binary and atom nodes.
 *
 * The tree structure can be then transformed using the standard fold catamorphism.
 */


/**
 * Interface implemented by elements of a tree.
 *
 * Note that we assume the tree node is immutable!
 *
 * The type parameter [Tree] indicates what is the least common supertype of all
 * tree nodes.
 *
 * Note that if a tree node isn't [Unary] or [Binary], it is assumed to be an atom without children.
 */
interface TreeNode<out Tree> {
    val node: Tree
}

/**
 * Common interface for unary operators. Used by [DirFormula] and [Formula].
 */
interface Unary<out This, Tree> : TreeNode<Tree> where This : Tree, Tree : TreeNode<Tree> {

    /**
     * The singular child element of this object.
     */
    val inner: Tree

    /**
     * Create a copy of the original object but optionally replace the child element.
     */
    fun copy(inner: Tree = this.inner): This

}

/**
 * Common interface for binary operators. Used by [Formula], [DirFormula] and [Expression].
 */
interface Binary<out This, Tree> : TreeNode<Tree> where This : Tree, Tree : TreeNode<Tree> {

    /**
     * The child of this element which is the root of the left subtree.
     */
    val left: Tree

    /**
     * The child of this element which is the root of the right subtree.
     */
    val right: Tree

    /**
     * Create a copy of the original object but optionally replace the left or right child element.
     */
    fun copy(left: Tree = this.left, right: Tree = this.right): This

}

/**
 * Standard tree fold. You can provide transformation operations for
 * atoms, unary and binary nodes.
 *
 * Useful for computing some aggregated function based on the tree structure or
 * when transforming this tree to a completely different tree.
 *
 * Compared to standard fold, we provide the current element as a target of an extension function,
 * not as additional function argument.
 */
fun <Tree : TreeNode<Tree>, R> TreeNode<Tree>.fold(
        atom: Tree.() -> R,
        unary: Unary<*, Tree>.(R) -> R,
        binary: Binary<*, Tree>.(R, R) -> R
) : R = when (this) {
    is Unary<*, Tree> -> unary.invoke(this, this.inner.fold(atom, unary, binary))
    is Binary<*, Tree> -> binary.invoke(this,
            this.left.fold(atom, unary, binary),
            this.right.fold(atom, unary, binary)
    )
    else -> atom.invoke(this.node)
}

/**
 * Change tree structure while preserving tree type. You can provide transformation operations for
 * atoms, unary and binary nodes. The only requirement is that operations
 * don't change the type of the tree.
 *
 * Useful for simple transformations, like tree optimisations, leaf transformations, etc.
 *
 * Note that the default transformations just copies the current tree structure
 * (the leaves are not copied since they should be immutable).
 */
@Suppress("RemoveExplicitTypeArguments")
fun <Tree: TreeNode<Tree>> TreeNode<Tree>.map(
        atom: Tree.() -> Tree = { this },
        unary: Unary<Tree, Tree>.(Tree) -> Tree = { this.copy(inner = it) },
        binary: Binary<Tree, Tree>.(Tree, Tree) -> Tree = { left, right -> this.copy(left, right)}
) = this.fold<Tree, Tree>(atom, unary, binary)