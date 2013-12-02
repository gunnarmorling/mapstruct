/**
 *  Copyright 2012-2013 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.test.collection.implicit;

import java.util.Arrays;

import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.MapperTestBase;
import org.mapstruct.ap.testutil.WithClasses;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

@WithClasses({ Order.class, OrderDto.class, OrderLine.class, OrderLineDto.class, OrderMapper.class })
public class ImplicitCollectionGenerationTest extends MapperTestBase {

    @Test(enabled = false)
    @IssueKey("3")
    public void shouldMapNullList() {
        //given
        OrderLine orderLine1 = new OrderLine();
        orderLine1.setDescription( "rubber tree" );

        OrderLine orderLine2 = new OrderLine();
        orderLine2.setDescription( "sequoia" );

        Order order = new Order();
        order.setOrderLines( Arrays.asList( orderLine1, orderLine2 ) );

        //when
        OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto( order );

        //then
        assertThat( orderDto ).isNotNull();
        assertThat( orderDto.getOrderLines() ).onProperty( "description" ).containsExactly( "rubber tree", "sequoia" );
    }
}
