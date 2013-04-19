/*******************************************************************************
 * Copyright 2013 Dominik Seichter
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sf.qualitytest.blueprint.configuration;

import net.sf.qualitytest.blueprint.CreationStrategy;
import net.sf.qualitytest.blueprint.MatchingStrategy;

/**
 * A pair of {@code MatchingStrategy} with accompanied {@code CreationStrategy}.
 * 
 * @author Dominik Seichter
 * 
 */
final class StrategyPair {

	private final MatchingStrategy matching;
	private final CreationStrategy<?> creation;

	public StrategyPair(final MatchingStrategy matching, final CreationStrategy<?> creation) {
		this.matching = matching;
		this.creation = creation;
	}

	public MatchingStrategy getKey() {
		return matching;
	}

	public CreationStrategy<?> getValue() {
		return creation;
	}

}
