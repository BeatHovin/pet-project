import {SteelGrade} from "./steelGrade";
import {ProductLength} from "./productLength";
import {ProfileSize} from "./profileSize";
import {SideThickness} from "./sideThickness";
import {Standard} from "./standard";

export class ProductType {

  constructor(
    public productTypeId: number,
    public productType: string,
    public steelGrades: SteelGrade[],
    public lengths: ProductLength[],
    public profileSizes: ProfileSize[],
    public sideThicknesses: SideThickness[],
    public standards: Standard[]

  ) {}

}
