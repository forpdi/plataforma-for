import React from "react";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

const GoalBoard = props => (
	<div className={props.className + " dashboard-goals-board"}>
		{
			props.loading
			?
			<div className="loading-text-goal-board">
				{Messages.getEditable("label.loading", "fpdi-nav-label")}
			</div>
			:
			<div>
				<div className="dashboard-goals-board-number">{props.numberValue}</div>
				<div>
					{
						props.numberValue !== 1
							? Messages.getEditable("label.goals", "fpdi-nav-label")
							: (Messages.getEditable("label.goalSing", "fpdi-nav-label"))
					}
				</div>
				<div>
					{
						props.numberValue !== 1
							? props.goalSubLabel
							: props.goalSubLabelSingular
					}
				</div>
			</div>
		}
	</div>
);

export default GoalBoard;
