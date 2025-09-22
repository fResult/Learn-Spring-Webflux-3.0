package com.fResult.rsocket.fp

/**
 * Function composition operator.
 *
 * Composes two functions into one — the result of [f] is passed to the receiver.
 * Useful when chaining transformations in a clean and readable way.
 *
 * ### Example:
 * ```
 * val addOne: (Int) -> Int = { it + 1 }
 * val double: (Int) -> Int = { it * 2 }
 * val result = double o addOne
 * println(result(3)) // Output: 8 → (3 + 1) * 2
 * ```
 *
 * @receiver A function that takes a [T2] and returns an [R].
 * @param f A function that takes a [T1] and returns a [T2].
 * @return A new function that takes a [T1] and returns an [R], by applying [f] then the receiver.
 *
 * @see
 * - also [then]
 * - <a href="https://en.wikipedia.org/wiki/Function_composition">Function composition (Wikipedia)</a>
 * @since 0.0.1
 */
infix fun <T1, T2, R> ((T2) -> R).o(f: (T1) -> T2): (T1) -> R = { x ->
  val g = this
  g(f(x))
}

/**
 * Function chaining operator.
 *
 * Chains two functions into one — the result of the receiver is passed to [f].
 * Useful when chaining transformations in a clean and readable way.
 *
 * ### Example:
 * ```
 * val addOne: (Int) -> Int = { it + 1 }
 * val double: (Int) -> Int = { it * 2 }
 * val result = addOne then double
 * println(result(3)) // Output: 8 → (3 + 1) * 2
 * ```
 *
 * @receiver A function that takes a [T1] and returns a [T2].
 * @param f A function that takes a [T2] and returns an [R].
 * @return A new function that takes a [T1] and returns an [R], by applying the receiver then [f].
 *
 * @see also [o]
 * @see <a href="https://en.wikipedia.org/wiki/Function_composition">Function composition (Wikipedia)</a>
 * @since 0.0.1
 */
infix fun <T1, T2, R> ((T1) -> T2).then(f: (T2) -> R): (T1) -> R = f o this
