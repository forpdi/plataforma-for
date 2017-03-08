package org.forpdi.planning.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.forpdi.core.filters.Filter;
import org.forpdi.dashboard.DashboardBS;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureHelper;
import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;

public class PeformanceFilter extends HibernateBusiness implements Filter<Long> {

	private PeformanceFilterType type;
	@Inject
	private StructureBS sbs;
	@Inject
	private AttributeHelper attrHelper;
	@Inject
	private StructureHelper structHelper;
	@Inject
	private DashboardBS dbs;

	public PeformanceFilterType getType() {
		return type;
	}

	public void setType(PeformanceFilterType type) {
		this.type = type;
	}

	@Override
	public List<Long> depurate(List<Long> list) {

		if (type == null) {
			LOGGER.info("Type of performance filter is NULL!");
			return list;
		}
		List<Long> ids = new ArrayList<>();
		for (Long id : list) {
			StructureLevelInstance instance = this.structHelper.retrieveLevelInstance(id);
			if (!instance.getLevel().isGoal()) {
				continue;
			}
			List<AttributeInstance> attrs = this.sbs.listAttributeInstanceByLevel(instance);
			Double max = null, min = null, exp = null, reach = null;
			Date finish = null;
			for (AttributeInstance attr : attrs) {
				if (attr.getAttribute().isExpectedField()) {
					exp = attr.getValueAsNumber();
				} else if (attr.getAttribute().isMaximumField()) {
					max = attr.getValueAsNumber();
				} else if (attr.getAttribute().isMinimumField()) {
					min = attr.getValueAsNumber();
				} else if (attr.getAttribute().isReachedField()) {
					reach = attr.getValueAsNumber();
				} else if (attr.getAttribute().isFinishDate()) {
					finish = attr.getValueAsDate();
				}
			}
			AttributeInstance polarity = this.attrHelper.retrievePolarityAttributeInstance(instance.getParent());
			Date today = new Date();
			switch (type) {
			case BELOW_MINIMUM:
				if ((reach == null && finish != null && finish.before(today))
						|| (reach != null && min != null && this.dbs.polarityComparison(polarity, min, reach))) {
					ids.add(id);
				}
				break;
			case BELOW_EXPECTED:
				if (reach != null && this.dbs.polarityComparison(polarity, exp, reach)
						&& (this.dbs.polarityComparison(polarity, reach, min) || Double.compare(reach, min) == 0)) {
					ids.add(id);
				}
				break;
			case ENOUGH:
				if (((this.dbs.polarityComparison(polarity, max, reach)
						&& (this.dbs.polarityComparison(polarity, reach, exp) || Double.compare(reach, exp) == 0))
						|| (reach != null && max != null && (Double.compare(reach, exp) == 0)))) {
					ids.add(id);
				}
				break;
			case ABOVE_MAXIMUM:
				if (this.dbs.polarityComparison(polarity, reach, max)
						|| (reach != null && max != null && (Double.compare(reach, max) == 0))) {
					ids.add(id);
				}
				break;
			case NOT_STARTED:
				if (reach == null && ((min == null && exp == null && max == null) || (finish.after(today)))) {
					ids.add(id);
				}
				break;
			default:
				LOGGER.info("Invalid type of performance filter!");
				ids = list;
				break;
			}
		}
		return ids;
	}

}
