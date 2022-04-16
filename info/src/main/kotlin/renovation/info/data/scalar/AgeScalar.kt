/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.info.data.scalar

import com.netflix.graphql.dgs.DgsScalar
import graphql.scalar.GraphqlIntCoercing

@DgsScalar(name = "Age")
class AgeScalar : GraphqlIntCoercing()
