import React from "react";
import TreeViewNode from "forpdi/jsx_forrisco/core/widget/treeview/TreeViewNode.jsx";
export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object
	},
	propTypes: {
		tree: React.PropTypes.array
	},
	idGenOffset: 100000,
	getInitialState() {
		return {
		};
	},
	componentDidMount() {
		var me = this;
	},
	componentWillUnmount() {

	},
	componentWillReceiveProps() {
	},

	onExpand(nodeProps) {
		if (typeof nodeProps.node.onExpand == 'function') {
			nodeProps.node.onExpand(nodeProps.node, nodeProps.nodeLevel);
		} else {
			nodeProps.expanded = true;
			this.forceUpdate();
		}
	},
	onShrink(nodeProps) {
		if (typeof nodeProps.node.onShrink == 'function') {
			nodeProps.node.onShrink(nodeProps.node, nodeProps.nodeLevel);
		} else {
			nodeProps.expanded = false;
			this.forceUpdate();
		}
	},

	renderNode(node, index, loop) {
		var me = this;
		loop = typeof loop == 'number' ? loop:0;
		return (
			<TreeViewNode
				{...node}
				key={"tree-node-"+loop+"-"+index}
				ref={"tree-node-"+loop+"-"+index}
				node={node}
				nodeIndex={index}
				nodeLevel={loop}
				onExpand={this.onExpand}
				onShrink={this.onShrink}
				>
					{node.children ? node.children.map((nextNode, nextIndex) => {
						return this.renderNode(nextNode, nextIndex, loop+1);
					}):""}
			</TreeViewNode>
		);
	},

	render() {
		return <div className="fpdi-treeview">
			{this.props.tree.map(this.renderNode)}
		</div>;
	}
});
