class Ship {
  const name;
  const weapon;
  const aura;
  const zen;

  constructor(name, weapon, aura, zen) {
    this.name = string(name);
    this.weapon = string(weapon);
    this.aura = string(aura);
    this.zen = string(zen);
  }
}
