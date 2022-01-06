pragma solidity ^0.4.25;
import "./PaillierPrecompiled.sol";
pragma experimental ABIEncoderV2;

contract CallPaillier {
    PaillierPrecompiled paillier;

    mapping(string => mapping(string => string)) votes;

    constructor() public {
        // 调用PaillierPrecompiled预编译合约
        paillier = PaillierPrecompiled(0x5003);
    }

    // paillier加法同态加密
    function add(string _cipher1, string _cipher2) public view returns (string) {
        return paillier.paillierAdd(_cipher1, _cipher2);
    }

    // 创建投票信息列表
    function createVote( string voteName, string[] names, string initCiphertext) public returns (string) {
        for (uint256 i = 0; i < names.length; i++) {
            votes[voteName][names[i]] = initCiphertext;
        }
        return "OK";
    }

    // 选票合计加密计算
    function addVote( string voteName, string[] names, string[] ciphertexts) public returns (string) {
        for (uint256 i = 0; i < names.length; i++) {
            votes[voteName][names[i]] = paillier.paillierAdd(votes[voteName][names[i]], ciphertexts[i]);
        }
        return "OK";
    }

    // 获取投票信息列表
    function getVote(string voteName, string[] names) public view returns (string[], string[]) {
        string[] memory values = new string[](names.length);
        for (uint256 i = 0; i < names.length; i++) {
            values[i] = votes[voteName][names[i]];
        }
        return (names, values);
    }
}
