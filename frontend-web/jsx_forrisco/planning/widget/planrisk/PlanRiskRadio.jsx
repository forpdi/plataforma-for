import React from 'react'

const PlanRiskRadio = props => {
  const id = `${props.name}-${props.label}`
  return (
    <div style={styles.containerStyle}>
      <input
        style={styles.radioStyle}
        type="radio"
        name={props.name}
        value={props.value}
        checked={props.checked}
        onChange={props.onChange}
        id={id}
      />
      <label htmlFor={id} style={styles.labelStyle}>{props.label}</label>
    </div>
  );
}

const styles = {
  containerStyle: {
    "display": "inline-block",
  },
  labelStyle: {
    "display": "inline-block",
    "color": "#70777b",
  },
  radioStyle: {
    "margin": "0px 5px",
    "height": "20px",
    "width": "20px",
  },
};

export default PlanRiskRadio;
