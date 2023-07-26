import {ProductLength} from "./productLength";
import {ProductType} from "./productType";
import {ProfileSize} from "./profileSize";
import {SideThickness} from "./sideThickness";
import {Standard} from "./standard";
import {SteelGrade} from "./steelGrade";

export class Product {
  public productId: number = null;
  public productLength: ProductLength = null;
  public productType: ProductType = null;
  public profileSize: ProfileSize= null;
  public quantity: number = null;
  public price: number = null;
  public sideThickness: SideThickness= null;
  public standard: Standard= null;
  public steelGrade: SteelGrade= null;
  constructor() {}


}
